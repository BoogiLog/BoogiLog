package com.example.boogilog

data class PostDto(private var id : String = "",
        private var postHead : String? = null,
        private var postBody : String? = null,
        private var imageUrl : String ? = null,
        private var postDate : String = "") {


    fun setId(id : String){
        this.id = id
    }

    fun getId() : String{
        return id;
    }

    fun setPostHead(postHead : String?){
        this.postHead = postHead
    }

    fun getPostHead() : String ?{
        return postHead
    }

    fun setPostBody(postBody : String?){
        this.postBody = postBody
    }

    fun getPostBody() : String ?{
        return postBody
    }

    fun setPostImgUrl(postImgUrl : String?){
        this.imageUrl = postImgUrl
    }

    fun getPostImgUrl() : String?{
        return imageUrl;
    }

    fun setPostDate(postDate : String){
        this.postDate = postDate
    }

    fun getPostDate() : String{
        return postDate
    }
}