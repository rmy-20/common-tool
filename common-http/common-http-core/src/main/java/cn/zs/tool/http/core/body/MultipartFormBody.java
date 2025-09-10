package cn.zs.tool.http.core.body;

import cn.zs.tool.http.core.body.multipart.Multipart;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 表单数据
 *
 * @author sheng
 */
public class MultipartFormBody implements Body {
    /**
     * 表单数据
     */
    private final List<Multipart> multipartList;

    /**
     * 创建#{@link MultipartFormBody}
     */
    public static MultipartFormBody create() {
        return new MultipartFormBody();
    }

    public MultipartFormBody() {
        this.multipartList = new ArrayList<>();
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        for (Multipart multipart : multipartList) {
            multipart.writeTo(outputStream);
        }
        outputStream.flush();
    }
}
