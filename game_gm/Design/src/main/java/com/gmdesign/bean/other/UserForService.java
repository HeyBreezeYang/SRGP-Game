package com.gmdesign.bean.other;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by DJL on 2017/3/3.
 *
 * @ClassName GM
 * @Description
 */
@Getter
@Setter
@ToString
@Data
public class UserForService {
    private String name;
    private int root;
    private String userKey;
}
