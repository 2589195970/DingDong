package com.ruoyi.common.utils.order;

import com.vdurmont.emoji.EmojiParser;

public class EmojiUtils {
    public static String removeEmoji(String str) {
        return EmojiParser.removeAllEmojis(str);
    }
}
