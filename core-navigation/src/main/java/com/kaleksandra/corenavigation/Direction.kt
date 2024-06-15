package com.kaleksandra.corenavigation

sealed class Direction(val path: String)
object MainDirection : Direction("main")
object TakePhotoDirection : Direction("take_photo")
object GalleryDirection : Direction("gallery")