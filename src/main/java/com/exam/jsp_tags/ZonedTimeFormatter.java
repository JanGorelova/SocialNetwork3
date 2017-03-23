package com.exam.jsp_tags;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.exam.logic.Constants.USER_ZONE_ID;

@Log4j
@Setter
@Getter
public class ZonedTimeFormatter extends TagSupport {
    private static final long serialVersionUID = 3081514849669679223L;
    private ZonedDateTime time;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        ZoneId zoneId = (ZoneId) pageContext.getSession().getAttribute(USER_ZONE_ID);
        if (zoneId == null) zoneId = ZoneId.of("UTC");
        ZonedDateTime zonedDateTime = time.toInstant().atZone(zoneId);
        try {
            out.write(formatter.format(zonedDateTime));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SKIP_BODY;
    }
}