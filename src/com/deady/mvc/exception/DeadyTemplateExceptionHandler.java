package com.deady.mvc.exception;

import java.io.Writer;

import org.apache.log4j.Logger;

import freemarker.core.Environment;
import freemarker.core.InvalidReferenceException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author Andre.Z 2014-11-5 上午11:48:53<br>
 * 
 *         自定义freemarker模板处理错误
 * 
 */
public class DeadyTemplateExceptionHandler implements TemplateExceptionHandler {

    private static Logger logger = Logger
            .getLogger(TemplateExceptionHandler.class);

    @Override
    public void handleTemplateException(TemplateException te, Environment env,
            Writer out) throws TemplateException {
        if (!env.isInAttemptBlock()) {
            if (te instanceof InvalidReferenceException) {
                // 不存在的变量引用，直接返回
                return;
            }
            // 其余错误，日志记录下，不继续抛错，使得页面继续渲染
            logger.error("模板解析出错: " + te.getMessage());
        }
    }

}
