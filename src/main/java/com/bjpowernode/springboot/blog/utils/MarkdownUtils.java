package com.bjpowernode.springboot.blog.utils;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.gfm.tables.internal.TableHtmlNodeRenderer;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

public class MarkdownUtils {

    /**
     * markDown 格式转成HTML格式
     */
    public static String markDownToHTML(String markDown){
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markDown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);  // ex: "<p>This is <em>Sparta</em></p>\n"
    }

    public static String markDownToHTMLExtensions(String markDown){
        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的html
        List<Extension> tableExtensions = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtensions)
                .build();
        Node document = parser.parse(markDown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtensions)
                .attributeProviderFactory(new AttributeProviderFactory() {
            @Override
            public AttributeProvider create(AttributeProviderContext context) {
                return new CustomAttributeProvider();
            }
        }).build();
        return renderer.render(document);
    }

    /**
     * 处理标签的属性
     */
    static class CustomAttributeProvider implements AttributeProvider{

        @Override
        public void setAttributes(Node node, String s, Map<String, String> map) {
            if (node instanceof Link){
                map.put("target","_blank");
            }
            if (node instanceof TableBlock){
                map.put("class","ui celled table");
            }
        }
    }


}
