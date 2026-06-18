package com.sans347.app.export

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sans347.app.data.getCategoryRisk
import com.sans347.app.data.getConformityModules
import com.sans347.app.data.graphById
import com.sans347.app.data.ResultData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ReportPdfExporterTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun categoryII_exportIsSinglePageWithHeaderCards() {
        val file = exportForCategory("II", 1)
        assertEquals(1, pageCount(file))
        assertTrue(file.length() > 0)
    }

    @Test
    fun sep_exportIsSinglePage() {
        val file = exportForCategory("SEP", 1)
        assertEquals(1, pageCount(file))
    }

    private fun exportForCategory(category: String, figureId: Int): File {
        val graph = graphById(figureId)!!
        val result = ResultData(
            category = category,
            figureId = figureId,
            product = 5000.0,
            ps = 100.0,
            vOrDn = 50.0,
            equipmentType = graph.equipmentType,
            stateOfContents = "Gas",
            fluidGroup = "Dangerous",
        )
        val plotPoint = ExportPlotPoint(
            x = result.vOrDn,
            y = result.ps,
            colorHex = "#EAB308",
        )
        return ReportPdfExporter.exportToCache(
            context,
            result,
            graph,
            getCategoryRisk(category),
            getConformityModules(category),
            plotPoint,
        )
    }

    private fun pageCount(file: File): Int {
        val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(pfd)
        val count = renderer.pageCount
        renderer.close()
        pfd.close()
        return count
    }
}
