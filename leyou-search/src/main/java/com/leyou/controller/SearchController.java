package com.leyou.controller;

import com.leyou.dto.SearchRequest;
import com.leyou.dto.SearchResult;
import com.leyou.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shen youjian
 * @date 2018/7/26 13:48
 */
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("page")
    public ResponseEntity<SearchResult> searchGoodsPage(@RequestBody SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        Integer page = searchRequest.getPage();
        Integer size = searchRequest.getSize();

        if (StringUtils.isBlank(key) || page == null || page < 0 || size == null || size < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            // elasticsearchRepository 提供的分页查询是从 0基 开始的
            SearchResult result = searchService.searchGoodsPage(searchRequest, page - 1, size);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
