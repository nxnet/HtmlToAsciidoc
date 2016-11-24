package io.nxnet.html2adoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

// TODO: Auto-generated Javadoc
/**
 * <p><b>Title: Html2AsciidocConverter </b></p>
 * <p><b>
 *    Description:
 *         
 * 
 * </b> </p>
 * <p><b>Copyright:</b> Copyright (c) ETK 2014 </p>
 * <p><b>Company:</b> Ericsson Nikola Tesla d.d.</p>
 *
 * @author  nruzic
 * @version PA1
 *          <p><b>Version History:</b></p>
 *          <br>PA1 Nov 24, 2016
 * @since   Nov 24, 2016  8:28:47 PM
 */
public class Html2AsciidocConverter
{
    
    /** The script engine. */
    ScriptEngine scriptEngine;

    /**
     * Instantiates a new html 2 asciidoc converter.
     */
    public Html2AsciidocConverter()
    {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        this.scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
    }

    /**
     * Instantiates a new html 2 asciidoc converter.
     *
     * @param scriptEngine the script engine
     */
    public Html2AsciidocConverter(ScriptEngine scriptEngine)
    {
        this.scriptEngine = scriptEngine;
    }

    /**
     * Convert.
     *
     * @param htmlString the html string
     * @return the string
     * @throws ConverterException the converter exception
     */
    public String convert(String htmlString) throws ConverterException
    {
        /*
         * Create javascript library reader
         */
        Reader jsLibraryReader = new BufferedReader(
                new InputStreamReader(this.getClass().getResourceAsStream("to-asciidoc.js"), StandardCharsets.UTF_8));

        /*
         * Read javascript library
         */
        try
        {
            this.scriptEngine.eval(jsLibraryReader);
        }
        catch (ScriptException e)
        {
            throw new ConverterException(e);
        }

        /*
         * Invoke javascript library method
         */
        Invocable scriptInvocable = (Invocable) this.scriptEngine;
        try
        {
            WebClient webClient = new WebClient();
            URL url = new URL("http://www.example.com");
            StringWebResponse webResponse = new StringWebResponse(
                    "<html><head><title>Test</title></head><body></body></html>", url);
            HtmlPage htmlPage = HTMLParser.parseHtml(webResponse, webClient.getCurrentWindow());
            String asciidocString = (String) scriptInvocable.invokeFunction("toAsciidoc", htmlString, htmlPage);
            return asciidocString;
        }
        catch (NoSuchMethodException e)
        {
            throw new ConverterException(e);
        }
        catch (ScriptException e)
        {
            throw new ConverterException(e);
        }
        catch (MalformedURLException e)
        {
            throw new ConverterException(e);
        }
        catch (IOException e)
        {
            throw new ConverterException(e);
        }
    }
}
