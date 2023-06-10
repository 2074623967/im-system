package com.lld.im.codec.pack.message;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tangcj
 * @date 2023/06/10 18:15
 **/
@Data
@NoArgsConstructor
public class RecallMessageNotifyPack {

    private String fromId;

    private String toId;

    private Long messageKey;

    private Long messageSequence;
}
