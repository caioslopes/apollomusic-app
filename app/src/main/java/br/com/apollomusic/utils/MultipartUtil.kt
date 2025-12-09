package br.com.apollomusic.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun getFileName(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex >= 0) {
                    result = cursor.getString(columnIndex)
                }
            }
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != null && cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result
}


/**
 * Converte uma URI (especialmente content://) em uma MultipartBody.Part para upload com Retrofit.
 * @param context O Context da aplicação.
 * @param uri A Uri do arquivo a ser enviado.
 * @param partName O nome da parte no formulário multipart (ex: "images").
 * @return Um MultipartBody.Part pronto para ser usado, ou null em caso de falha.
 */
fun uriToMultipartBodyPart(context: Context, uri: Uri, partName: String): MultipartBody.Part? {
    return try {
        // Abre um stream para ler o conteúdo da URI
        val inputStream: InputStream = context.contentResolver.openInputStream(uri) ?: return null

        // Cria um arquivo temporário no diretório de cache do app
        val fileName = getFileName(context, uri) ?: "temp_file"
        val file = File(context.cacheDir, fileName)

        // Copia o conteúdo do inputStream para o arquivo temporário
        val outputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        // Cria o RequestBody a partir do arquivo temporário
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

        // Cria a MultipartBody.Part
        MultipartBody.Part.createFormData(partName, file.name, requestBody)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
