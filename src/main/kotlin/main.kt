import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.io.path.*

fun main(args: Array<String>) {
    val installDir = getInstallLocation()
    if (installDir == null) {
        println("ERROR: Unsupported Operating System this tool only supports Windows, Linux, and OSX")
        return
    }
    val mcDataDir = Paths.get(installDir)
    if (!mcDataDir.exists()) {
        println("ERROR: Could not find minecraft install at ${mcDataDir.toAbsolutePath()}???")
        return
    }

    val versions = mcDataDir.resolve("versions")
    if (!versions.exists()) {
        println("ERROR: No versions directory found??? Have you run Minecraft before?")
        return
    }

    println("STARTING PATCHING")
    println("Please note this may take longer if you have slower hardware or lots of minecraft version\n")

    val entries = versions.listDirectoryEntries()
    for (path in entries) {
        if (!path.isDirectory()) continue
        val files = path.listDirectoryEntries()
        for (file in files) {
            if (file.extension == "jar") {
                val tmpJar = path.resolve("${file.fileName}.tmp")
                val relPath = file.relativeTo(versions).toString()
                print("\rLocated jar file '$relPath'")
                val input = ZipInputStream(file.inputStream())
                val output = ZipOutputStream(tmpJar.outputStream())
                try {
                    print("\rPatching jar file to tmp path '${tmpJar.fileName}'")
                    output.use { _ ->
                        input.use { input ->
                            var entry: ZipEntry?
                            while (true) {
                                entry = input.nextEntry ?: break
                                output.putNextEntry(entry)
                                if (entry.name.equals("log4j2.xml")) {
                                    val bytes = ByteArrayOutputStream()
                                    input.copyTo(bytes, 4096)
                                    var text = String(bytes.toByteArray(), StandardCharsets.UTF_8)
                                    text = text.replace("%msg", "%msg{nolookups}")
                                    output.write(text.toByteArray(StandardCharsets.UTF_8))
                                    output.closeEntry()
                                } else {
                                    input.copyTo(output, 4096)
                                    output.closeEntry()
                                }
                            }
                        }
                    }
                    print("\rPatching complete to '${tmpJar.fileName}'")
                    if (tmpJar.exists()) {
                        print("\rReplacing original jar with patched jar")
                        tmpJar.moveTo(file, overwrite = true)
                        print("\rReplaced original jar")
                    } else {
                        println("ERROR: Temporary jar didnt exist for version ${file.fileName}")
                    }
                } catch (e: IOException) {
                    println("ERROR: Failed to replace log4j config for version ${file.fileName}")
                }
            }
        }
    }

    println("\nPATCHING COMPLETE")
    println("If you did not see any errors appear throughout the process then you are all clear")
}

fun getInstallLocation(): String? {
    val osName = System.getProperty("os.name").lowercase()
    return if (osName.contains("win")) {
        System.getProperty("user.home") + "/AppData/Roaming/.minecraft"
    } else if (osName.contains("mac")) {
        System.getProperty("user.home") + "/Library/Application Support/minecraft"
    } else if (osName.contains("nux")) {
        System.getProperty("user.home") + "/.minecraft"
    } else {
        null
    }
}