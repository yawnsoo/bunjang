package com.example.demo.src.jjim.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchPostCollectionReq {

    private int jjim_id;
    private int to_jjim_collection_id;
}
