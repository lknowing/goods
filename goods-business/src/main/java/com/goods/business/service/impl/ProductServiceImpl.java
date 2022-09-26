package com.goods.business.service.impl;

import com.goods.business.mapper.ProductMapper;
import com.goods.business.mapper.ProductStockMapper;
import com.goods.business.service.ProductService;
import com.goods.common.model.business.Product;
import com.goods.common.model.business.ProductStock;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 20:59
 * @FileName: ProductServiceImpl
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductStockMapper productStockMapper;

    @Override
    public PageVO<ProductVO> findProductList(Integer pageNum, Integer pageSize, ProductVO productVO) {
        String name = productVO.getName();
        Integer status = productVO.getStatus();
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        if (status != null) {
            criteria.andEqualTo("status", status);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andLike("name", "%" + name + "%");
        }
        if (productVO.getCategoryKeys() != null) {
            Long[] categoryKeys = productVO.getCategoryKeys();
            if (categoryKeys != null && categoryKeys.length == 3) {
                criteria.andEqualTo("oneCategoryId", categoryKeys[0]);
                criteria.andEqualTo("twoCategoryId", categoryKeys[1]);
                criteria.andEqualTo("threeCategoryId", categoryKeys[2]);
            }
        }
        List<Product> productList = productMapper.selectByExample(example);
        List<ProductVO> productVoList = productList.stream().map(product -> {
            ProductVO productVo = new ProductVO();
            BeanUtils.copyProperties(product, productVo);
            return productVo;
        }).collect(Collectors.toList());
        List<ProductVO> page = ListPageUtils.page(productVoList, pageSize, pageNum);
        return new PageVO<>(productVoList.size(), page);
    }

    @Override
    public void addProduct(ProductVO productVO) {
        // 新增
        // 商品编号
        productVO.setPNum(UUID.randomUUID().toString());
        // 时间
        productVO.setCreateTime(new Date());
        productVO.setModifiedTime(new Date());
        // 设置默认状态 2:添加待审核
        productVO.setStatus(2);
        // 读取分类数组
        Long[] categoryKeys = productVO.getCategoryKeys();
        productVO.setOneCategoryId(categoryKeys[0]);
        productVO.setTwoCategoryId(categoryKeys[1]);
        productVO.setThreeCategoryId(categoryKeys[2]);
        // 赋值到实体类
        Product product = new Product();
        BeanUtils.copyProperties(productVO, product);
        productMapper.insert(product);
    }

    @Override
    public void publish(Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setStatus(0);
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public void remove(Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setStatus(1);
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public void delete(Long productId) {
        productMapper.deleteByPrimaryKey(productId);
        Product product = productMapper.selectByPrimaryKey(productId);
        String pNum = product.getPNum();
        Example example = new Example(ProductStock.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("pNum", pNum);
        productStockMapper.deleteByExample(example);
    }

    @Override
    public void back(Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setStatus(0);
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public ProductVO edit(Long productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        return productVO;
    }

    @Override
    public void update(Long productId, ProductVO productVO) {
        productVO.setModifiedTime(new Date());
        Product product = new Product();
        BeanUtils.copyProperties(productVO, product);
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public PageVO<ProductStockVO> findProductStocks(Integer pageNum, Integer pageSize, String name, String categorys) {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 0);
        if (!StringUtils.isEmpty(name)) {
            criteria.andLike("name", "%" + name + "%");
        }
        if (!StringUtils.isEmpty(categorys)) {
            String[] split = categorys.split(",");
            criteria.andEqualTo("oneCategoryId", split[0]);
            criteria.andEqualTo("twoCategoryId", split[1]);
            criteria.andEqualTo("threeCategoryId", split[2]);
        }
        List<Product> productList = productMapper.selectByExample(example);
        List<ProductStockVO> stockVoList = productList.stream().map(product -> {
            ProductStock productStock = new ProductStock();
            productStock.setPNum(product.getPNum());
            ProductStock stock = productStockMapper.selectOne(productStock);
            ProductStockVO productStockVO = new ProductStockVO();
            if (stock != null) {
                BeanUtils.copyProperties(product, productStockVO);
                productStockVO.setStock(stock.getStock());
            }
            return productStockVO;
        }).collect(Collectors.toList());
        List<ProductStockVO> collect = stockVoList.stream().filter(productStockVO -> {
            return productStockVO.getStock() != null;
        }).collect(Collectors.toList());
        List<ProductStockVO> page = ListPageUtils.page(collect, pageSize, pageNum);
        return new PageVO<>(stockVoList.size(), page);
    }

    @Override
    public List<ProductStockVO> findAllStocks(Integer pageNum, Integer pageSize, String name, String categorys) {
        PageVO<ProductStockVO> productStocks = findProductStocks(pageNum, pageSize, name, categorys);
        List<ProductStockVO> productStockVOList = productStocks.getRows();
        return productStockVOList;
    }
}
