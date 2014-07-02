package com.donal.superne.app.model.register;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by jianghaofeng on 14-7-2.
 */
public class RegistIQProvider implements IQProvider
{
    @Override
    public IQ parseIQ(XmlPullParser parser) throws Exception
    {

        RegistrationIQ iq = new RegistrationIQ();

        boolean done = false;
        while (!done)
        {
            int eventType = parser.next();
            if (eventType == XmlPullParser.END_TAG)
            {
                if (parser.getName().equals("data"))
                {
                    done = true;
                }
            }
            else
            {
                if (eventType == XmlPullParser.START_TAG)
                {
                    iq.setCode(parser.getAttributeValue("", "code"));
                    iq.setMsg(parser.getAttributeValue("", "msg"));
                }
            }
        }
        return iq;
    }


}
