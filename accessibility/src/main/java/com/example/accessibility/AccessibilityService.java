package com.example.accessibility;

import static android.os.SystemClock.sleep;
import static com.example.accessibility.LunchApp.startApkName;
import static com.example.accessibility.adb.shell;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

@SuppressLint("NewApi") // 安卓7.0后才支持手势
public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
    private final static String TAG = "AccessibilityService";
    public static AccessibilityService sAccessibility = null;

    public static AccessibilityService accessibility() {
        return sAccessibility;
    }

    @Override
    protected boolean onKeyEvent(KeyEvent paramKeyEvent) {
        return false;//如果返回true，就会导致其他应用接收不到事件了，但是对KeyEvent的修改是不会分发到其他应用中的！
    }


    /**
     * 处理通知栏信息 * * 如果是微信红包的提示信息,则模拟点击 * * @param event
     */
    private void handleNotification(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();
        if (!texts.isEmpty()) {
            for (CharSequence text : texts) {
                String content = text.toString(); //如果微信红包的提示信息,则模拟点击进入相应的聊天窗口
                if (content.contains("[微信红包]")) {
                    if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                        Notification notification = (Notification) event.getParcelableData();
                        PendingIntent pendingIntent = notification.contentIntent;
                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (source != null) {
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                Log.i(TAG, "topActivity:" + event.getClassName().toString());
            }
        }
    }

    /**
     * 部分操作 findAccessibilityNodeInfosByText 处出现空指针，此方法用于忽略空指针, 以免中断测试
     *
     * @param text 查找text id信息
     * @return AccessibilityNodeInfo
     */
    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String text) {
        List<AccessibilityNodeInfo> list;
        try {   // 防止空异常导致测试中断
            AccessibilityNodeInfo node = AccessibilityService.accessibility().getRootInActiveWindow();
            list = node.findAccessibilityNodeInfosByText(text);
            node.recycle();
        } catch (Exception ignored) {
            Log.e("testNg", "无障碍 findAccessibilityNodeInfosByText 空指针异常");
            sleep(100);
            list = findAccessibilityNodeInfosByText(text);  // 异常后 100ms 间隔递归执行
        }
        return list;
    }

    /**
     * 通过text查找节点信息
     *
     * @param text 查找text节点信息
     * @return text节点信息
     */
    public AccessibilityNodeInfo findNodeByText(String text) {
        List<AccessibilityNodeInfo> list = findAccessibilityNodeInfosByText(text);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 模拟点击事件
     *
     * @param x 横坐标像素点
     * @param y 纵坐标像素点
     */
    public static void tap(int x, int y) {
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(x, y);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 30L));
        GestureDescription gesture = builder.build();
        accessibility().dispatchGesture(gesture, new android.accessibilityservice.AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);
    }

    /**
     * 模拟滑动事件
     *
     * @param x1   x
     * @param y1   y
     * @param x2   x
     * @param y2   y
     * @param time 滑动时长
     */
    private void swipe(int x1, int y1, int x2, int y2, final int time) {
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0L, time));
        GestureDescription gesture = builder.build();
        accessibility().dispatchGesture(gesture, new android.accessibilityservice.AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
            }
        }, null);
    }

    /**
     * @param id   要滑动的id
     * @param down true 往下滑动
     */
    public boolean swipeNode(String id, boolean down) {
        AccessibilityNodeInfo node = findNodeById(id);
        if (node != null) {
            if (down) {
                return node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            } else {
                return node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            }
        }
        return false;
    }

    /**
     * @param node 点击 node
     */
    public void clickNode(AccessibilityNodeInfo node) {
        if (!node.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
            AccessibilityNodeInfo parent = node.getParent();
            while (parent != null) {
                if (parent.isClickable()) {
                    if (parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)) return;
                }
                parent = parent.getParent();
            }
            Rect rect = new Rect();
            node.getBoundsInScreen(rect);
            tap((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2);
        }
    }

    /**
     * 点击屏幕上的text
     *
     * @param text 点击的text
     * @param tap  使用shell tap 点击
     * @param con  不需要完全一致也能点击
     * @param all  屏幕范围外也能点击
     * @return 点击结果
     */
    public boolean clickNodeText(String text, boolean tap, boolean con, boolean all) {
        boolean result = clickNodeTextRecurs(getRootInActiveWindow(), text, tap, con, all);
        if (!result) {
            Log.i("testNg", "clickRecurs无法找到text:" + text + ", 正在尝试clickList点击");
            result = clickListNodeText(text, tap, con, all);
        }
        return result;
    }

    /**
     * 递归方式点击text
     *
     * @param node AccessibilityNodeInfo
     * @param text 点击的text
     * @param tap  使用shell tap 点击
     * @param con  不需要完全一致也能点击
     * @param all  屏幕范围外也能点击
     * @return 点击结果
     */
    public boolean clickNodeTextRecurs(AccessibilityNodeInfo node, String text, boolean tap,
                                       boolean con, boolean all) {
        if (node == null) return false;
        if (node.getChildCount() == 0) return clickNodeInfoText(node, text, tap, con, all);
        else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    if (clickNodeTextRecurs(node.getChild(i), text, tap, con, all)) return true;
                }
            }
        }
        return false;
    }

    /**
     * 查找方式点击text
     *
     * @param text 点击的text
     * @param tap  使用shell tap 点击
     * @param con  不需要完全一致也能点击
     * @param all  屏幕范围外也能点击
     * @return 点击结果
     */
    public boolean clickListNodeText(String text, boolean tap, boolean con, boolean all) {
        List<AccessibilityNodeInfo> list = findAccessibilityNodeInfosByText(text);
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                if (clickNodeInfoText(list.get(j), text, tap, con, all)) return true;
            }
        }
        return false;
    }

    /**
     * 统一text点击方式
     *
     * @param node AccessibilityNodeInfo
     * @param text 点击的text
     * @param tap  使用shell tap 点击
     * @param con  不需要完全一致也能点击
     * @param all  屏幕范围外也能点击
     * @return 点击结果
     */

    public boolean clickNodeInfoText(AccessibilityNodeInfo node, String text, boolean tap,
                                     boolean con, boolean all) {
        if (node.getText() != null) {
            boolean result = con && node.getText().toString().contains(text);
            if (result || node.getText().toString().equals(text)) {// 根据text内容进入
                Rect rect = new Rect();
                node.getBoundsInScreen(rect);
                if (all || ((rect.left >= 0 && rect.right >= 0 && rect.top >= 0 && rect.bottom >= 0))) {// 根据屏幕区域进入
                    if (tap) {
                        int x = (rect.left + rect.right) / 2;
                        int y = (rect.top + rect.bottom) / 2;
                        Log.i("testNg", "rect:x" + x + "   y:" + y);
                        tap(x, y);
                    } else clickNode(node);
                    Log.i("testNg", "clickText:" + node.getText() + " rect:" + rect +
                            " id:" + node.getViewIdResourceName() + " className:" + node.getClassName());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查找屏幕上的text
     *
     * @param text 查找的text
     * @param con  不需要完全一致也能查找
     * @param all  屏幕范围外也能查找
     * @return 查找结果
     */
    public boolean findNodeText(String text, boolean con, boolean all) {
        boolean result = findNodeTextRecurs(getRootInActiveWindow(), text, con, all);
        if (!result) {
//            Log.i("testNg", "findNodeText()无法找到text:" + text + ", 正在尝试findListText()查找");
            result = findListNodeText(text, con, all);
        }
        return result;
    }

    /**
     * 递归方式查找屏幕上的text
     *
     * @param node AccessibilityNodeInfo
     * @param text 查找的text
     * @param con  不需要完全一致也能查找
     * @param all  屏幕范围外也能查找
     * @return 查找结果
     */
    public boolean findNodeTextRecurs(AccessibilityNodeInfo node, String text, boolean con,
                                      boolean all) {
        if (node == null) return false;
        if (node.getChildCount() == 0) return findNodeInfoText(node, text, con, all);
        else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    if (findNodeTextRecurs(node.getChild(i), text, con, all)) return true;
                }
            }
        }
        return false;
    }

    /**
     * find方式查找屏幕上的text
     *
     * @param text 查找的text
     * @param con  不需要完全一致也能查找
     * @param all  屏幕范围外也能查找
     * @return 查找结果
     */
    public boolean findListNodeText(String text, boolean con, boolean all) {
        List<AccessibilityNodeInfo> list = findAccessibilityNodeInfosByText(text);
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                if (findNodeInfoText(list.get(j), text, con, all)) return true;
            }
        }
        return false;
    }

    /**
     * 统一查找text方法
     *
     * @param node AccessibilityNodeInfo
     * @param text 查找的text
     * @param con  不需要完全一致也能查找
     * @param all  屏幕范围外也能查找
     * @return 查找结果
     */
    boolean findNodeInfoText(AccessibilityNodeInfo node, String text, boolean con, boolean all) {
        if (node.getText() != null) {
            boolean result = (con && node.getText().toString().contains(text));
            if (result || node.getText().toString().equals(text)) {// 根据text内容进入
                Rect rect = new Rect();
                node.getBoundsInScreen(rect);
                if (all || (rect.left >= 0 && rect.right >= 0 && rect.top >= 0 && rect.bottom >= 0)) {
                    Log.i("testNg", "findText:" + node.getText() + " rect:" + rect +
                            " id:" + node.getViewIdResourceName() + " className:" + node.getClassName());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 点击屏幕上的描述信息
     *
     * @param desc 点击参数
     * @param tap  adb shell tap 方式点击
     * @param con  不需要一致也能点击
     * @param all  屏幕范围外也能点击
     * @return 点击结果
     */
    public boolean clickNodeDesc(String desc, boolean tap, boolean con, boolean all) {
        return nodeDesc(getRootInActiveWindow(), desc, tap, con, all);
    }

    public boolean nodeDesc(AccessibilityNodeInfo node, String desc, boolean tap, boolean con,
                            boolean all) {
        if (node.getChildCount() == 0) {
            if (node.getContentDescription() != null) {
                boolean result = con && node.getContentDescription().toString().contains(desc);
                if (result || node.getContentDescription().toString().equals(desc)) {   // 根绝描述内容
                    Rect rect = new Rect();
                    node.getBoundsInScreen(rect);
                    Log.i("testNg", "点击 desc:" + node.getContentDescription() + " rect:" + rect +
                            " id:" + node.getViewIdResourceName() + " className:" + node.getClassName());

                    if (all || ((rect.left >= 0 && rect.right >= 0 && rect.top >= 0 && rect.bottom >= 0))) { // 根据屏幕区域点击
                        if (!tap) {
                            tap((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2);
                        } else clickNode(node);
                        return true;
                    }
                }
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    if (nodeDesc(node.getChild(i), desc, tap, con, all)) return true;
                }
            }
        }
        return false;
    }

    /**
     * 查找屏幕上的描述信息, 需要比对完全一致
     *
     * @param desc 描述信息
     * @return true: 信息存在
     */
    public boolean findNodeDesc(String desc) {
        return findNodeDesc(getRootInActiveWindow(), desc);
    }

    public boolean findNodeDesc(AccessibilityNodeInfo node, String desc) {
        if (node.getChildCount() == 0) {
            if (node.getContentDescription() != null) {
                if (node.getContentDescription().toString().equals(desc)) {   // 根绝描述内容
                    Rect rect = new Rect();
                    node.getBoundsInScreen(rect);
                    Log.i("testNg", "找到 desc:" + node.getContentDescription() + " rect:" + rect +
                            " id:" + node.getViewIdResourceName() + " className:" + node.getClassName());
                    // 根据屏幕区域点击
                    return rect.left >= 0 && rect.right >= 0 && rect.top >= 0 && rect.bottom >= 0;
                }
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    if (findNodeDesc(node.getChild(i), desc)) return true;
                }
            }
        }
        return false;
    }

    /**
     * 点击屏幕上的ID控件
     *
     * @param id  要点击的ID控件
     * @param tap adb shell tap 方式点击
     * @param all 屏幕外也能点击
     * @return 返回点击结果
     */
    public boolean clickNodeId(String id, boolean tap, boolean all) {
        AccessibilityNodeInfo node = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = node.findAccessibilityNodeInfosByViewId(id);
        node.recycle();
        if (list != null && list.size() > 0) {
            for (AccessibilityNodeInfo item : list) {
                Log.i("testNg", "clickNodeId getViewIdResourceName:" + item.getViewIdResourceName());
                if (item.getViewIdResourceName() != null) {
                    Rect rect = new Rect();
                    item.getBoundsInScreen(rect);
                    if (all || ((rect.left >= 0 && rect.right >= 0 && rect.top >= 0 && rect.bottom >= 0))) {
                        if (tap) {
                            tap((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2);
                        } else clickNode(item);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查找屏幕上的ID控件
     *
     * @param id 要查找的ID控件
     * @return 返回查找结果
     */
    public boolean findNodeId(String id) {
        AccessibilityNodeInfo node = getRootInActiveWindow();
        if (node != null) {
            List<AccessibilityNodeInfo> list = node.findAccessibilityNodeInfosByViewId(id);
            node.recycle();
            for (AccessibilityNodeInfo ignored : list) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找屏幕上ID控件的坐标位置信息
     *
     * @param id 要查找的ID控件
     * @return 返回查找结果
     */
    public Rect findNodeIdRect(String id) {
        AccessibilityNodeInfo node = getRootInActiveWindow();
        if (node != null) {
            List<AccessibilityNodeInfo> list = node.findAccessibilityNodeInfosByViewId(id);
            node.recycle();
            for (AccessibilityNodeInfo item : list) {
                Rect r = new Rect();
                item.getBoundsInScreen(r);
                return r;
            }
        }
        return null;
    }

    /**
     * 清除所有任务栏的应用，仅保留自身
     *
     * @param id    点击id
     * @param leave 描述应用跳过清除
     * @return 返回清除结果
     */
    public boolean clickNodeIdCleanOther(String id, String leave) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        boolean result = false;
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                if (!item.getContentDescription().toString().contains(leave)) {
                    clickNode(item);
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 任务栏清除指定app
     *
     * @param id   点击清除的id
     * @param name 需要清除的app名称
     * @return 清除结果
     */
    public boolean clickNodeIdCleanSelect(String id, String name) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        boolean result = false;
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                if (item.getContentDescription().toString().contains(name)) {
                    clickNode(item);
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 设置->应用程序->列表内滑动查找app
     *
     * @param appName 根据app名称查找并点击
     * @return 返回找到结果
     */
    public boolean setAppListFindAppName(String appName) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByViewId("com.android.settings:id/apps_list");
        rootNode.recycle();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Rect rect = new Rect();
                list.get(i).getBoundsInScreen(rect);
                if (rect.left >= 0 && rect.right >= 0 && rect.top >= 0 && rect.bottom >= 0) {
                    AccessibilityNodeInfo node = list.get(i);
                    if (node != null) {
                        for (int j = 0; j < node.getChildCount(); j++) {
                            if (node.getChild(j).getChild(1).getChild(0).getText() != null) {
                                if (node.getChild(j).getChild(1).getChild(0).getText().toString().contains(appName)) {
                                    Log.i("testNg", "列表找到appName,尝试点击");
                                    if (node.getChild(j) != null) {
                                        clickNode(node.getChild(j));
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检查横竖屏适配结果
     *
     * @param text 出入适配参考内容
     * @return 结果为true：横屏适配
     */
    public boolean findNodeOrient(String text) {
        List<AccessibilityNodeInfo> list = findAccessibilityNodeInfosByText(text);
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getText() != null) {
                    Rect rect = new Rect();
                    list.get(j).getBoundsInScreen(rect);
                    if (list.get(j).getText().toString().equals(text)) {
                        if ((rect.left >= 0 && rect.right >= 0 && rect.top >= 0 && rect.bottom >= 0)) {
                            return Math.abs(rect.right - rect.left) > Math.abs(rect.bottom - rect.top);
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @return 当前屏幕包名
     */
    public String getTopPackage() {
        AccessibilityNodeInfo node = getRootInActiveWindow();
        Log.i("TAG", "Accessibility getTopPackage:" + node.getPackageName());
        return node.getPackageName().toString();
    }


    /**
     * @param id 获取id的text信息
     * @return text
     */
    public String findTextById(String id) {
        AccessibilityNodeInfo node = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = node.findAccessibilityNodeInfosByViewId(id);
        node.recycle();
        if (list.size() > 0) {
            AccessibilityNodeInfo nodeInfo = list.get(0);
            if (nodeInfo != null) {
                if (nodeInfo.getText() != null) {
                    return nodeInfo.getText().toString();
                }
            }
        }
        return null;
    }

    /**
     * @param id 找到id节点
     * @return id节点信息
     */
    public AccessibilityNodeInfo findNodeById(String id) {
        AccessibilityNodeInfo node = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = node.findAccessibilityNodeInfosByViewId(id);
        node.recycle();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Rect rect = new Rect();
                list.get(i).getBoundsInScreen(rect);
                if (rect.left >= 0 && rect.right >= 0 && rect.top >= 0 && rect.bottom >= 0) {
                    return list.get(i);
                }
            }
        }
        return null;
    }

    //辅助功能是否启动
    public static boolean isStart() {
        return sAccessibility != null;
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "服务已经中断", Toast.LENGTH_SHORT).show();
        sAccessibility = null;
    }

    public static boolean rebootState = false;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        sAccessibility = this;
        // 注册监听桌面变化事件，用于获取topActivity， 此方法第一次安装事件无效
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;   // Just in case this helps
        setServiceInfo(new AccessibilityServiceInfo());

        new Thread(() -> {
            sleep(500);
            Log.i("testNg", "服务已经启动1");
            if (findNodeText("模拟手动", true, false)) {
                Log.i("testNg", "服务已经启动");
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                sleep(100);
                if (findNodeText("autoTest", false, false))
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                startApkName("autoTest");
            } else {
                shell("input keyevent 224");// 亮屏
                sleep(3000);
                shell("input keyevent 82");// 解锁
                sleep(1000);
                shell("input keyevent 82");// 多解锁一次，防止一遍不一定解锁成功
                sleep(3000);
//                LunchApp.startActivity(BACK_APK_TEXT_ACTIVITY);
                rebootState = true;
            }
        }).start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "服务已经关闭", Toast.LENGTH_SHORT).show();
        sAccessibility = null;
    }
}
