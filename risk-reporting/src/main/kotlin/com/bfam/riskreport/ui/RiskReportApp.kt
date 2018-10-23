package com.bfam.riskreport.ui

import tornadofx.App
import tornadofx.launch

class RiskReportApp : App(RiskReportView::class)

fun main(args: Array<String>) {
    launch<RiskReportApp>(args)
}