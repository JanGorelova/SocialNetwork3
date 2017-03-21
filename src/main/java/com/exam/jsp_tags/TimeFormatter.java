package com.exam.jsp_tags;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.exam.logic.Constants.ZONE_ID;

@Log4j
@Setter
@Getter
public class TimeFormatter extends TagSupport {
    private Instant time;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm dd-MM-yyyy");

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        ZoneId zoneId = (ZoneId) pageContext.getSession().getAttribute(ZONE_ID);
        if (zoneId == null) zoneId = ZoneId.of("UTC");
        ZonedDateTime zonedDateTime = time.atZone(zoneId);
        try {
            out.write(formatter.format(zonedDateTime));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SKIP_BODY;
    }
}