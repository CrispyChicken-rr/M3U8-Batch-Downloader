import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

public class main {
    public static void main(String args[]){
        //填入文件路径
        String filePath = "C:\\Users\\Lee\\Desktop\\111\\测试.txt";
        //设置文件名
        transferMU(readBat(filePath),"测试");
    }

    // 把文件整理成url数组：把每一行数据 “s” 的url取出组成url数组 “urlArray”
    public static ArrayList readBat(String fileName){
        ArrayList<String> urlArray = new ArrayList<String>();
        try {
            InputStream fis = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String s;
            String temp = new String();
            char item;
            while ((s = br.readLine()) != null){   // 取文件的每一行数据为 String s
                while (true){       // 遍历每一行的第一个“到第一个”为一个字符串放进数组urlArray
                    for (int i = 0; i<s.length();i++){
                        item = s.charAt(i);
                        if (item == 34){
                            while (s.charAt(++i)!=34) {
                                temp += s.charAt(i);
                            }
                            break;
                        }
                    }
                    break;
                }
                urlArray.add(temp);
                temp = "";
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlArray;
    }

    public static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null,"M3U8批量下载器【by:逍遥一仙】  V1.4");

    //将urlArray里的url自动填入下载器
    public static void transferMU(ArrayList URLin,String name) {
        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null,"M3U8批量下载器【by:逍遥一仙】  V1.4");
        if (hwnd == null) {
            System.out.println("M3U8批量下载器未在运行");
        } else {
            User32.INSTANCE.ShowWindow(hwnd, 9);
            User32.INSTANCE.SetForegroundWindow(hwnd);
        }
            WinDef.RECT mu_rect = new WinDef.RECT();
            User32.INSTANCE.GetWindowRect(hwnd, mu_rect);
            int mu_width = mu_rect.right - mu_rect.left;
            int mu_height = mu_rect.bottom - mu_rect.top;
            User32.INSTANCE.MoveWindow(hwnd, 700, 100, mu_width, mu_height, true);

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String urlmu = "";
        String courname = "";
        for (int i=0;i<URLin.size();i++){
            //自动填  “文件/链接”
            urlmu = (String) URLin.get(i);
            clipboard.setContents(new StringSelection(urlmu), null);
            copyFunc(clipboard, 1);    // “文件/链接” 为 1

            //自动填  “文件名”
            courname = name+i;
            clipboard.setContents(new StringSelection(courname), null);
            copyFunc(clipboard, 2);    // “文件名” 为 2

            //自动点击 “添加”
            copyFunc(null, 3); // “添加” 为 3

        }
    }

    /*  功能：在指定位置自动填入url、文件名等；请在1234后分别更改鼠标坐标
    *   1为 “文件/链接”
    *   2为 “文件名”
    *   3为 “添加”
    *   4为 “全部开始”  （暂未使用，请自便）
    * */
    public static void copyFunc(Clipboard clipboard,int i){  // i是鼠标位
        switch (i){
            case 1:
                robot.mouseMove(500,110);  // “文件/链接” 框内坐标
                robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.delay(100);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.delay(100);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.delay(300);
                break;

            case 2:
                robot.mouseMove(500,150);  // “文件名” 框内坐标
                robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.delay(100);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.delay(100);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.delay(300);
                break;

            case 3:
                robot.mouseMove(730,130);  // “添加” 框内坐标
                robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                break;

            case 4:
                robot.mouseMove(730,360);  // “全部开始” 框内坐标
                robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                break;
        }
    }
}
