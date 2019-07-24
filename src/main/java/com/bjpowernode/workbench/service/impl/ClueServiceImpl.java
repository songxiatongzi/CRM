package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.settings.dao.UserDao;
import com.bjpowernode.settings.domain.User;
import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.SqlSessionUtil;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.workbench.dao.*;
import com.bjpowernode.workbench.domain.*;
import com.bjpowernode.workbench.service.ClueService;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/17 22:52
 * <p>
 * 功能描述：
 */
public class ClueServiceImpl implements ClueService {

    //线索业务层
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean createClue(Clue c) {

        boolean flag = true;

        int count = clueDao.createClue(c);

        if(count == 0){

            flag = false;

        }

        return flag;
    }

    @Override
    public Clue detail(String id) {

        //根据线索id 查询单条
        Clue c = clueDao.detail(id);

        return c;
    }

    @Override
    public boolean unbund(String id) {
        //根据关联关系表的id,解除关联关系

        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if(count == 0){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Clue> getClueList() {

        //查询所有的线索列表
        List<Clue> clueList = clueDao.getClueList();

        return clueList;
    }

    @Override
    public boolean bund(String clueId, String[] activityIds) {

        boolean flag = true;

        //在业务层，遍历数组，并创建对象
        for(String activityId : activityIds){

            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setActivityId(activityId);
            clueActivityRelation.setClueId(clueId);

            int count = clueActivityRelationDao.bund(clueActivityRelation);

            if(count == 0){
                flag = false;
            }

        }

        return flag;
    }

    @Override
    public boolean convent(String clueId, Tran tran, String createBy) {

        //交易装换业务共有十张标的操作
        boolean flag = true;

        //创建时间
        String createTime = DateTimeUtil.getSysTime();

        //取得线索的对象，从线索中取出公司人相关的信息，生成客户，取出与人相关的，生成联系人
        Clue clue = clueDao.getClueById(clueId);

        //通过线索对象提取公司的信息，当客户不存在的时候，创建客户(客户名称要精确匹配)
        String company = clue.getCompany();
        //就像对象一样，取出来的客户需要使用
        Customer customer = customerDao.getByName(company);

        if(customer == null){

            //之前没有创建客户,将线索中的信息取出，装进客户中，
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setCreateTime(clue.getCreateTime());
            customer.setName(clue.getCompany());
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(clue.getContactSummary());
            customer.setAddress(clue.getAddress());

            int count1 =  customerDao.save(customer);

            if(count1 != 1 ){

                flag = false;

            }

        }

        //客户已经创建
        //通过线索对象提取联系人的信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());

        int count2 = contactsDao.save(contacts);

        if(count2 != 1){

            flag = false;

        }

        //线索备注转换到客户备注以及联系人备注
        //查询与该线索关联的备注信息列表 clueId 是 clue_remark的外键
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);

        for(ClueRemark clueRemark : clueRemarkList){

            //取得每一条需要转换的备注信息(将该备注转换到客户 备注以及联系人备注中去)
            String noteContent = clueRemark.getNoteContent();

            //创建客户备注对象
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setNoteContent(noteContent);
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setCreateTime(createTime);
            customerRemark.setCreateBy(createBy);

            //添加客户备注信息
            int count3 = customerRemarkDao.saveCustomerRemark(customerRemark);

            if(count3 != 1){
                flag = false;
            }

            //添加联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();

            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setEditFlag("0");
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setContactsId(contacts.getId());

            int count4 = contactsRemarkDao.saveContactsRemark(contactsRemark);
            if(count4 != 1 ){

                flag = false;

            }

        }

        // “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //查询出与线索关联的市场活动列表
        //得到市场活动的id
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getActivityListByClueId(clueId);

        //遍历列表的到市场活动的id
        for(ClueActivityRelation activityRelation : clueActivityRelationList){

            String activityId =  activityRelation.getActivityId();

            //通过市场活动id 与联系人做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());

            //添加联系人和市场活动关联管理列表
            int count5 =  contactsActivityRelationDao.saveContactsActivityRelation(contactsActivityRelation);

            if(count5 != 1){

                flag = false;

            }

        }

        //如果有创建交易需求，创建一条交易(根据交易是不是为空，为空则创建一条交易)
        if(tran != null){

            tran.setSource(clue.getSource());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(contacts.getId());

            //创建一笔交易
            int count6 =  tranDao.saveTran(tran);
            if(count6 != 1 ){
                flag = false;
            }

            //在创建交易的同时，创建一笔交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setCreateBy(createBy);
            tranHistory.setTranId(tran.getId());
            tranHistory.setStage(tran.getStage());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateTime(createTime);

            //添加一笔交易历史
            int count7 = tranHistoryDao.saveTranHistory(tranHistory);
            if(count7 != 1 ){
                flag = false;
            }
        }

        //删除线索备注
        //通过clueId 删除线索备注
        int count8 =  clueRemarkDao.deleteClueRemarkByClueId(clueId);

        if(count8 != 1){

            flag = false;

        }

        //删除线索和市场活动的关系
        //通过线索id 得到市场活动列表，一个线索可以关联多个市场活动
        for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){

            int count9 =  clueActivityRelationDao.delete(clueActivityRelation.getClueId());

            if(count9 != 1){
                flag = false;
            }
        }

        //删除线索
        //通过线索id删除线索
        int count10 =  clueDao.delteClueByClueId(clueId);
        if(count10 != 1){

            flag = false;

        }


        return flag;
    }
}
