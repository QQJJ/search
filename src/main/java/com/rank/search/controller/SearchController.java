package com.rank.search.controller;

import com.rank.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/8/10 16:11
 */
@Controller
@RequestMapping("rank")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("search")
    public void rankSearch(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        searchService.rankSearch(file, request, response);
    }

    @RequestMapping("downLoadModel")
    public void downLoadModel(HttpServletRequest request, HttpServletResponse response) {
        searchService.downLoadModel(request, response);
    }
}
