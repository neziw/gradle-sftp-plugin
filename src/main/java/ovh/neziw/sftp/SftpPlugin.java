/*
 * This file is part of "gradle-sftp-plugin", licensed under MIT License.
 *
 *  Copyright (c) 2025 neziw
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package ovh.neziw.sftp;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class SftpPlugin implements Plugin<Project> {

    private static final String TASK_NAME = "sftpUpload";

    @Override
    public void apply(final Project target) {
        target.getTasks().register(TASK_NAME, SftpTask.class, task -> {
            task.getHost().set("your-host.com");
            task.getPort().set(22);
            task.getUsername().set("your-username");
            task.getPassword().set("your-password");
            task.getLocalFile().set(target.file("build/libs/your-library.jar"));
            task.getRemotePath().set("/path/to/remote/your-library.jar");
        });
    }
}