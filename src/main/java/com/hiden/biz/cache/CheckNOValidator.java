package com.hiden.biz.cache;

import com.hiden.biz.common.BizConstants;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by myron on 16-9-30.
 */
@Service
public class CheckNOValidator {

    @Resource
    private MemcachedClient memcachedClient;

    public boolean checkNOisValid(String code, String mobile) {
        boolean valid = false;
        String codeCached = (String) memcachedClient.get(BizConstants.CHECK_NO_PREFIX + mobile);//jedis.get(BizConstants.CHECK_NO_PREFIX + mobile);
        if (null != code && StringUtils.equals(code, codeCached)) {
            valid = true;
        }
        return valid;
    }
}
