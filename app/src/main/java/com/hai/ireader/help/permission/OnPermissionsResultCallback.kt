package com.hai.ireader.help.permission

interface OnPermissionsResultCallback {

    fun onPermissionsGranted(requestCode: Int)

    fun onPermissionsDenied(requestCode: Int, deniedPermissions: Array<String>)

}