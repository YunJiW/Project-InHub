package com.twenty.inhub.boundedContext.post.entity;

import com.twenty.inhub.boundedContext.community.entity.Community;
import com.twenty.inhub.boundedContext.member.entity.Member;
import com.twenty.inhub.boundedContext.post.dto.PostDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private int postHits;

    @Column
    @CreationTimestamp
    private LocalDateTime createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne(fetch = LAZY)
    private Member member;

    public String getAuthorNickname() {
        if (member != null) {
            return member.getNickname();
        }
        return null;
    }

    public static Post toSaveEntity(PostDto postDto, Member member) {
        Post build = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .postHits(0)
                .member(member)
                .build();

        member.getPosts().add(build);
        return build;
    }
}
