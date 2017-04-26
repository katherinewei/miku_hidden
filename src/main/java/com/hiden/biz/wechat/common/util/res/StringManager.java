
package com.hiden.biz.wechat.common.util.res;

import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.*;

public class StringManager {
    private static int LOCALE_CACHE_SIZE = 10;
    private final ResourceBundle bundle;
    private final Locale locale;
    private static final Map<String, Map<Locale, StringManager>> managers = new Hashtable();

    private StringManager(String packageName, Locale locale) {
        String bundleName = packageName + ".LocalStrings";
        ResourceBundle bnd = null;

        try {
            bnd = ResourceBundle.getBundle(bundleName, locale);
        } catch (MissingResourceException var9) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if(cl != null) {
                try {
                    bnd = ResourceBundle.getBundle(bundleName, locale, cl);
                } catch (MissingResourceException var8) {
                    ;
                }
            }
        }

        this.bundle = bnd;
        if(this.bundle != null) {
            Locale bundleLocale = this.bundle.getLocale();
            if(bundleLocale.equals(Locale.ROOT)) {
                this.locale = Locale.ENGLISH;
            } else {
                this.locale = bundleLocale;
            }
        } else {
            this.locale = null;
        }

    }

    public String getString(String key) {
        String str;
        if(key == null) {
            str = "key may not have a null value";
            throw new IllegalArgumentException(str);
        } else {
            str = null;

            try {
                if(this.bundle != null) {
                    str = this.bundle.getString(key);
                }
            } catch (MissingResourceException var4) {
                str = null;
            }

            return str;
        }
    }

    public String getString(String key, Object... args) {
        String value = this.getString(key);
        if(value == null) {
            value = key;
        }

        MessageFormat mf = new MessageFormat(value);
        mf.setLocale(this.locale);
        return mf.format(args, new StringBuffer(), (FieldPosition)null).toString();
    }

    public Locale getLocale() {
        return this.locale;
    }

    public static final synchronized StringManager getManager(String packageName) {
        return getManager(packageName, Locale.getDefault());
    }

    public static final synchronized StringManager getManager(String packageName, Locale locale) {
        Map<Locale,StringManager> map = managers.get(packageName);
        if(map == null) {
            map = new LinkedHashMap<Locale,StringManager>(LOCALE_CACHE_SIZE, 1.0F, true) {
                private static final long serialVersionUID = 1L;

                protected boolean removeEldestEntry(Map.Entry<Locale, StringManager> eldest) {
                    return this.size() > StringManager.LOCALE_CACHE_SIZE - 1;
                }
            };
            managers.put(packageName, map);
        }

        StringManager mgr = (StringManager)((Map)map).get(locale);
        if(mgr == null) {
            mgr = new StringManager(packageName, locale);
            ((Map)map).put(locale, mgr);
        }

        return mgr;
    }

    public static StringManager getManager(String packageName, Enumeration<Locale> requestedLocales) {
        while(true) {
            if(requestedLocales.hasMoreElements()) {
                Locale locale = (Locale)requestedLocales.nextElement();
                StringManager result = getManager(packageName, locale);
                if(!result.getLocale().equals(locale)) {
                    continue;
                }

                return result;
            }

            return getManager(packageName);
        }
    }
}
