package com.sans347.app.export

import android.content.Context
import com.sans347.app.data.GraphConfig
import java.io.File
import java.io.FileOutputStream

object GraphPngExporter {
    fun exportToCache(
        context: Context,
        config: GraphConfig,
        plotPoint: ExportPlotPoint? = null,
    ): File {
        val bitmap = GraphBitmapRenderer.render(config = config, plotPoint = plotPoint)
        val file = File(context.cacheDir, "sans347-figure-${config.id}-graph.png")
        FileOutputStream(file).use { output ->
            bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, output)
        }
        bitmap.recycle()
        return file
    }
}
