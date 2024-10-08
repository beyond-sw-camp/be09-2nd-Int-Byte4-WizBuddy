package com.intbyte.wizbuddy.comment.repository;

import com.intbyte.wizbuddy.comment.domain.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment findBySubsCodeAndEmployeeCode(int subsCode, String employeeCode);

    Comment findBySubsCodeAndCommentAdoptedState(int subsCode, boolean commentAdoptedState);
}
