package com.example.hrdepartmentclient.ui.worker

import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.models.Worker

interface OnItemClickListener {
    fun onItemClick(worker: Worker)
}