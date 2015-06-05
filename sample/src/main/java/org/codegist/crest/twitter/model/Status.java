/*
 * Copyright 2011 CodeGist.org
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *  ===================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.twitter.model;

import org.codegist.common.lang.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class Status {
    //@JsonProperty("created_at")  Date createdAt,
    @JsonProperty("favorited")
    boolean favorited;
    @JsonProperty("id")
    private long id;
    @JsonProperty("in_reply_to_screen_name")
    private String inReplyToScreenName;
    @JsonProperty("in_reply_to_status_id")
    private long inReplyToStatusId;
    @JsonProperty("in_reply_to_user_id")
    private long inReplyToUserId;
//    @JsonProperty("retweet_count")
//    private long retweetCount;
    @JsonProperty("retweeted")
    private boolean retweeted;
    @JsonProperty("source")
    private String source;
    @JsonProperty("text")
    private String text;
    @JsonProperty("truncated")
    private boolean truncated;
    @JsonProperty("user")
    private User user;
    @JsonProperty("retweeted_status")
    private Status retweetedStatus;

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("inReplyToScreenName", inReplyToScreenName)
                .append("inReplyToStatusId", inReplyToStatusId)
                .append("inReplyToUserId", inReplyToUserId)
                .append("retweeted", retweeted)
                .append("source", source)
                .append("text", text)
                .append("truncated", truncated)
                .append("user", user)
                .append("retweetedStatus", retweetedStatus)
                .toString();
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    public long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public void setInReplyToStatusId(long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public long getInReplyToUserId() {
        return inReplyToUserId;
    }

    public void setInReplyToUserId(long inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getRetweetedStatus() {
        return retweetedStatus;
    }

    public void setRetweetedStatus(Status retweetedStatus) {
        this.retweetedStatus = retweetedStatus;
    }
}
