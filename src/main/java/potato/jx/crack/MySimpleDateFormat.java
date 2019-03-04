package potato.jx.crack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author     : zh_zhou
 * Create at  : 2017/12/31 16:02
 * Description:
 */
public class MySimpleDateFormat extends SimpleDateFormat {
    private static final long serialVersionUID = -2680960935177697658L;
    private final SimpleDateFormat df;

    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

    public MySimpleDateFormat(SimpleDateFormat df) {
        this.df = df;
    }

    public MySimpleDateFormat(String pattern, Locale locale) {
        super(pattern, locale);
        df = null;
    }

    @Override
    public Date parse(String source) throws ParseException {
        assert df != null;
        Date date = df.parse(source);
        if (dt.format(date).equals("2019-02-13")) {
            date = df.parse("01-01-2100");
        }
        return date;
    }
}

