package com.rank.search.entity;

import java.io.Serializable;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/8/9 17:56
 */
public class KeyWordRank implements Serializable {

    private static final long serialVersionUID = 7230839428530724868L;

    private String keyword;

    private Integer rankSoso;

    private Integer rankSosoW;

    private Integer rank360;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getRankSoso() {
        return rankSoso;
    }

    public void setRankSoso(Integer rankSoso) {
        this.rankSoso = rankSoso;
    }

    public Integer getRankSosoW() {
        return rankSosoW;
    }

    public void setRankSosoW(Integer rankSosoW) {
        this.rankSosoW = rankSosoW;
    }

    public Integer getRank360() {
        return rank360;
    }

    public void setRank360(Integer rank360) {
        this.rank360 = rank360;
    }
}
