package com.gpmall.shopping.controller;

import com.gpmall.commons.result.ResponseData;
import com.gpmall.commons.result.ResponseUtil;
import com.gpmall.search.ProductSearchService;
import com.gpmall.search.dto.SearchRequest;
import com.gpmall.search.dto.SearchResponse;
import com.gpmall.shopping.constants.ShoppingRetCode;
import com.gpmall.shopping.form.SearchPageInfo;
import com.gpmall.user.annotation.Anoymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商城全部商品搜索和热门推荐
 *
 * @author jin
 * @version v1.0.0
 * @Date 2019年8月11日
 */
@RestController
@RequestMapping("/shopping")
@Api(tags = "SearchController", description = "搜索控制层")
public class SearchController {
    @Reference(timeout = 30000)
    private ProductSearchService productSearchService;

//    @Anoymous
    @PostMapping("/search")
    @ApiOperation("搜索商品")
    @ApiImplicitParam(name = "pageInfo", value = "搜索入参", dataType = "SearchPageInfo")
    public ResponseData<SearchResponse> searchProduct(@RequestBody SearchPageInfo pageInfo) {
        SearchRequest request = new SearchRequest();
        request.setKeyword(pageInfo.getKey());
        request.setCurrentPage(pageInfo.getPage());
        request.setPageSize(pageInfo.getSize());
        request.setPriceGt(pageInfo.getPriceGt());
        request.setPriceLte(pageInfo.getPriceLte());
        request.setSort(pageInfo.getSort());
        SearchResponse response = productSearchService.search(request);
        if (response.getCode().equals(ShoppingRetCode.SUCCESS.getCode())) {
            return new ResponseUtil().setData(response.getData());
        }
        return new ResponseUtil().setErrorMsg(response.getMsg());
    }

    @Anoymous
    @GetMapping("/searchHotWord")
    @ApiOperation("搜索热词")
    public ResponseData<SearchResponse> getSearchHotWord() {
        SearchResponse searchResponse = productSearchService.hotProductKeyword();
        if (searchResponse.getCode().equals(ShoppingRetCode.SUCCESS.getCode())) {
            return new ResponseUtil().setData(searchResponse.getData());
        }
        return new ResponseUtil().setErrorMsg(searchResponse.getMsg());
    }
}
