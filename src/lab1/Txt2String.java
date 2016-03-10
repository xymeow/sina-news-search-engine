package lab1;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * Created by xymeow on 15/10/27.
 */
public class Txt2String {
    /**
     * 将文本文件中的内容读入到buffer中
     */
    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }
    /**
     * 读取文本文件内容
     */
    public static String readFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        Txt2String.readToBuffer(sb, filePath);
        return sb.toString();
    }
}