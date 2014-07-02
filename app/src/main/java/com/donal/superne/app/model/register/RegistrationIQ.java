package com.donal.superne.app.model.register;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by jianghaofeng on 14-7-2.
 */
public class RegistrationIQ extends IQ
{
    private String msg;
    private String code;

    public static final String NAMESPACE = "superne:iq:register";

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String text)
    {
        this.msg = text;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    @Override
    public CharSequence getChildElementXML()
    {
        XmlStringBuilder xml = new XmlStringBuilder();

        if (this.getType() == Type.GET)
        {
            xml.halfOpenElement("query");
            xml.xmlnsAttribute(NAMESPACE);
            if (code != null)
            {
                xml.attribute("code", code);
            }
            xml.rightAngelBracket();
            xml.append(msg);
            xml.closeElement("query");
        }
        else if (this.getType() == Type.RESULT)
        {
            xml.halfOpenElement("result");
            xml.xmlnsAttribute(NAMESPACE);
            xml.attribute("code", code);
            xml.attribute("msg", msg);
            xml.rightAngelBracket();
            xml.closeElement("result");
        }
        return xml;
    }
}
