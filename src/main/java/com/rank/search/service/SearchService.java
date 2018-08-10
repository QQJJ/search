package com.rank.search.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/8/10 16:13
 */
public interface SearchService {

    void rankSearch(MultipartFile file, HttpServletRequest request, HttpServletResponse response);

    void downLoadModel(HttpServletRequest request, HttpServletResponse response);
}
