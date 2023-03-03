package com.example.mapapp.model

data class SiteLocation(
    val lat: Double,
    val long: Double,
    val siteId: Int = 0,
    val siteName: String
)


object SiteData{
    val data1 = SiteLocation(
        lat = 13.87,
        long = 83.95,
        siteId = 156,
        siteName = "Site1"
    )

    val data2 = SiteLocation(
        lat = 13.082680,
        long = 80.270721,
        siteId = 241,
        siteName = "Chennai"
    )
    val data3 = SiteLocation(
        lat = 12.916517,
        long = 79.132500,
        siteId = 843,
        siteName = "Vellore"
    )
    val data4 = SiteLocation(
        lat = 12.971599,
        long = 77.594566,
        siteId = 42,
        siteName = "Bengaluru"
    )
    val data5 = SiteLocation(
        lat = 17.3850,
        long = 78.4867,
        siteId = 786,
        siteName = "Hyderabad"
    )
}

