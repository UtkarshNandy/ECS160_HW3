package com.ecs160.hw2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class TopPosts {

   public List<Post> getTopPosts(String resourceName) throws Exception {
       ParseAndCreate newParser = new ParseAndCreate();
       List<Post> newPosts = newParser.parsePosts(resourceName);
       List<Post> topLikedPosts;
       int[] indexes = new int[10];
       // Set each element to -1
       Arrays.fill(indexes, -1);
       int indexPointer = 0;

       PriorityQueue<Post> topPosts = new PriorityQueue<>((a, b) -> Integer.compare(a.getLikeCount(), b.getLikeCount()));

       for (Post post : newPosts) {
           if (topPosts.size() < 10) {
               topPosts.add(post);
           } else if (post.getLikeCount() > topPosts.peek().getLikeCount()) {
               topPosts.poll();  // Remove the post with the least likes
               topPosts.add(post);
           }
       }

       List<Post> topPostsList = new ArrayList<>(topPosts);

       return topPostsList;
   }
}
