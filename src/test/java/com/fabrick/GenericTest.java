package com.fabrick;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author fabio.sgroi
 */
@Slf4j
public class GenericTest {
    
    protected final String API_KEY_OK = "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP";
    protected final Long ACCOUNT_ID_OK = 14537780L;
    
    protected final String API_KEY_KO = "API-KEY-NOT-VALID";
    protected final Long ACCOUNT_ID_KO = 14537781L;
    
    protected Date StringToDate(String s) {
        Date result = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            result = dateFormat.parse(s);
        } catch (ParseException e) {
            log.error("ErrorStringToDate: ", e);
        }
        return result;
    }
}
