package com.bjpowernode.springboot.blog.service.impl;

import com.bjpowernode.springboot.blog.dao.AvatarRepository;
import com.bjpowernode.springboot.blog.dao.CommentRepository;
import com.bjpowernode.springboot.blog.entity.Avatar;
import com.bjpowernode.springboot.blog.entity.Comment;
import com.bjpowernode.springboot.blog.exception.NotFoundException;
import com.bjpowernode.springboot.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Transactional
    @Override
    public List<Comment> listCommentsByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");
        List<Comment> comments =  commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        return eachComment(comments);

    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root 根节点，blog不为空的集合
     */
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComment();
            for (Comment reply : replys1) {
                //循环迭代，找出子代，存放到tempReplys中
                recursively(reply);
            }
            //修改顶级节点的reply集合为迭代处理后的结果集合
            comment.setReplyComment(tempReplys);
            //清除暂时存放
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);
        if (comment.getReplyComment().size()>0){
            List<Comment> replys = comment.getReplyComment();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComment().size()>0){
                    recursively(reply);
                }
            }
        }
    }


    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1){
            Optional<Comment> byId = commentRepository.findById(parentCommentId);
            if (byId.isPresent()) {
                comment.setParentComment(byId.get());
            }else{
                throw new NotFoundException("Comment Parrent Not Found");
            }
        }else{
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public String getRandomAvatar() {
        long randomNumber = (long) ((Math.random()*((avatarRepository.count()-1)+1))+1);
        Optional<Avatar> avatar =  avatarRepository.findById(randomNumber);
        if (avatar.isPresent()){
            return avatar.get().getAvatarPath();
        }else{
            throw new NotFoundException("Avatar Not Found");
        }
    }
}
