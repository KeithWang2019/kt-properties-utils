package com.cangsg.brick.kt.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cangsg.brick.kt.annotation.KtValidateRegex;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public final class KtValidateRegexHandler extends KtValidator<KtValidateRegex, String> {

    static final Map<String, Pattern> patternMap = new ConcurrentHashMap<>();

    @Override
    public KtValidateResult<?> call(String val, KtValidateRegex annotation) throws RuntimeException {
        // 空由NotEmpty负责
		if (val == null) {
			return null;
		} else {
			Pattern p = getPattern(annotation.regex());
			Matcher m = p.matcher(val);
			if (m.find()) {
				return null;
			}
		}

		return KtValidateResult.build(annotation.message(), this.getFieldPath());
    }

    public static Pattern getPattern(String regex) {
        if (regex == null) {
            return null;
        }
        Pattern p = patternMap.get(regex);
        if (p != null) {
            return p;
        }
        p = Pattern.compile(regex);
        patternMap.put(regex, p);
        return p;
    }

}
