package com.lld.im.common.model;

import lombok.Data;

import java.util.List;

/**
 * @author tangcj
 * @date 2023/06/10 11:54
 **/
@Data
public class SyncResp<T> {

    private Long maxSequence;

    private boolean isCompleted;

    private List<T> dataList;
}
