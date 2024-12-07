/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.logging;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import org.apache.logging.log4j.util.Strings;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogBleedingTask implements AssignmentEndpoint {

  private static final Logger log = LoggerFactory.getLogger(LogBleedingTask.class);
  private final String password;

  public LogBleedingTask() {
    this.password = UUID.randomUUID().toString();
    log.info(
        "Password for admin: {}",
        Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8)));
  }

  @PostMapping("/LogSpoofing/log-bleeding")
  @ResponseBody
  public AttackResult completed(@RequestParam String username, @RequestParam String password) {
    if (Strings.isEmpty(username) || Strings.isEmpty(password)) {
      return failed(this).output("Please provide username (Admin) and password").build();
    }

    if (username.equals("Admin") && password.equals(this.password)) {
      return success(this).build();
    }

    return failed(this).build();
  }
}