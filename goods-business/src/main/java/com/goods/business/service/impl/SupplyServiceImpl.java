package com.goods.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.converter.SupplyConverter;
import com.goods.business.mapper.SupplyMapper;
import com.goods.business.service.SupplyService;
import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * title:
 *
 * @Author xu
 * @Date 2022/09/23 16:00
 * @FileName: SupplyServiceImpl
 */
@Service
public class SupplyServiceImpl implements SupplyService {
    @Autowired
    private SupplyMapper supplyMapper;

    @Autowired
    private SupplyConverter supplyConverter;

    @Override
    public PageVO<SupplierVO> findSupplierList(int pageNum, int pageSize, SupplierVO supplierVO) {
        PageHelper.startPage(pageNum, pageSize);
        Example o = new Example(Supplier.class);
        String name = supplierVO.getName();
        String address = supplierVO.getAddress();
        String contact = supplierVO.getContact();
        Example.Criteria criteria = o.createCriteria();
        if (name != null && !"".equals(name)) {
            criteria.andLike("name", "%" + name + "%");
        }
        if (address != null && !"".equals(address)) {
            criteria.andLike("address", "%" + address + "%");
        }
        if (contact != null && !"".equals(contact)) {
            criteria.andLike("contact", "%" + contact + "%");
        }
        List<Supplier> supplierList = supplyMapper.selectByExample(o);
        List<SupplierVO> supplierVOS = supplyConverter.converterToSupplierVOList(supplierList);
        PageInfo<Supplier> info = new PageInfo<>(supplierList);
        return new PageVO<>(info.getTotal(), supplierVOS);
    }

    @Override
    public void add(SupplierVO supplierVO) {
        if (supplierVO != null) {
            Supplier supplier = new Supplier();
            BeanUtils.copyProperties(supplierVO, supplier);
            supplier.setCreateTime(new Date());
            supplier.setModifiedTime(new Date());
            supplyMapper.insert(supplier);
        }
    }

    @Override
    public SupplierVO findById(Long supplierId) {
        Supplier supplier = supplyMapper.selectByPrimaryKey(supplierId);
        if (supplier != null) {
            SupplierVO supplierVO = new SupplierVO();
            BeanUtils.copyProperties(supplier, supplierVO);
            return supplierVO;
        }
        return null;
    }

    @Override
    public void update(Long supplierId, SupplierVO supplierVO) {
        if (supplierVO != null) {
            Supplier supplier = new Supplier();
            BeanUtils.copyProperties(supplierVO, supplier);
            supplier.setModifiedTime(new Date());
            supplyMapper.updateByPrimaryKey(supplier);
        }
    }

    @Override
    public void delete(Long supplierId) {
        supplyMapper.deleteByPrimaryKey(supplierId);
    }


}
