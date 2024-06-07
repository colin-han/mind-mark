package info.colinhan.mindmark;

import info.colinhan.mindmark.model.MMModel;
import info.colinhan.mindmark.model.MMNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class XMindConverter {
    public void convert(MMModel model, Path output) {
        Path tmp = output.resolve("tmp");
        try {
            deleteDirectoryRecursively(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tmp.toFile().mkdirs();

        copyTemplateToTemp(tmp);
        generateContent(tmp, model);
        zipOutput(tmp, output.resolve(model.getName() + ".xmind"));
    }

    private static void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private void zipOutput(Path folder, Path outputFile) {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(outputFile))) {
            Files.walk(folder)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(folder.relativize(path).toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Failed to zip file: " + path);
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateContent(Path tempPath, MMModel model) {
        try {
            var content = XMindConverter.class.getClassLoader().getResourceAsStream("templates/xmind/content.json");
            var json = new JSONArray(new String(content.readAllBytes(), StandardCharsets.UTF_8));
            var root = (JSONObject) json.get(0);
            var rootTopic = (JSONObject) root.get("rootTopic");
            rootTopic.put("title", model.getName());
            var children = (JSONArray) ((JSONObject) rootTopic.get("children")).get("attached");
            children.clear();
            model.getNodes()
                    .forEach(node -> this.generateNode(children, node));

            Files.writeString(tempPath.resolve("content.json"), json.toString(2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateNode(JSONArray children, MMNode node) {
        JSONObject child = new JSONObject();
        child.put("id", UUID.randomUUID());
        child.put("title", node.getText());
        JSONArray labels = new JSONArray();
        if (node.getEstimation() != null) {
            labels.put("⏱" + node.getEstimation().toString());
        }
        node.getAssignees().forEach(
                a -> labels.put("🙍‍" + a)
        );
        node.getTags().forEach(
                t -> labels.put("🏷" + t)
        );
        if (!labels.isEmpty()) {
            child.put("labels", labels);
        }
        if (node.getChildCount() > 0) {
            var attachedChildren = new JSONArray();
            node.getChildren().forEach(c -> generateNode(attachedChildren, c));
            var nodeChildren = new JSONObject();
            nodeChildren.put("attached", attachedChildren);
            child.put("children", nodeChildren);
        }
        children.put(child);
    }

    private void copyTemplateToTemp(Path tempFolder) {
        try {
            copyTemplateTo(tempFolder, "manifest.json");
            copyTemplateTo(tempFolder, "metadata.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void copyTemplateTo(Path tempFolder, String fileName) throws IOException {
        ClassLoader classLoader = XMindConverter.class.getClassLoader();
        try (var file1 = classLoader.getResourceAsStream("templates/xmind/" + fileName)) {
            if (file1 == null) throw new IOException("File not found!");
            Files.copy(file1, tempFolder.resolve(fileName));
        }
    }
}
