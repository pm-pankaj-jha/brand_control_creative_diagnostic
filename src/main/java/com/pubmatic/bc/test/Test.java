package com.pubmatic.bc.test;

import java.io.IOException;
import java.net.MalformedURLException;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

      

        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.setRefreshHandler(new ThreadedRefreshHandler()); // This is to allow handling the page operation using threads else an exception will pop up
        try {
            HtmlPage page = webClient.getPage("http://v1000.vn/bang-xep-hang?ref=bang-xep-hang-1000-doanh-nghiep-dong-thue-thu-nhap-nhieu-nhat-2012");
            HtmlAnchor link = page.getFirstByXPath("//a[@href='javascript:loadRankingTable(3)']");
                        link.click();
                        System.out.println(page.getTextContent());

        } catch (FailingHttpStatusCodeException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*
        wc.crawl("http://v1000.vn/bang-xep-hang?ref=bang-xep-hang-1000-doanh-nghiep-dong-thue-thu-nhap-nhieu-nhat-2012");

        for (String url:wc.urlList)
        {
            pc.crawl(url);
        }
        */
    }
}