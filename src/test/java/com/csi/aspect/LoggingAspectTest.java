package com.csi.aspect;

import com.csi.controller.EmployeeController;
import com.csi.dto.EmployeeDTO;
import com.csi.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith({SpringExtension.class, OutputCaptureExtension.class})
@DisplayName("Logging Aspect Tests")
class LoggingAspectTest {

    @Autowired
    private EmployeeController employeeController;

    @Test
    @DisplayName("Should log service method entry and exit")
    void testServiceMethodLogging(CapturedOutput output) {
        EmployeeDTO employeeDTO = new EmployeeDTO("Rebecca V", "United Stated Of America",
                "9852364714", "285699.62", new Date(2001-06-23), "'rebv@gmail.com");
        employeeController.updateData(3,  employeeDTO);
        String logOutput = output.toString();
        assertThat(logOutput).contains("→ [CONTROLLER]");
        assertThat(logOutput).contains("← [CONTROLLER]");
        assertThat(logOutput).contains("EmployeeController.updateData()");
        assertThat(logOutput).contains("ms");
    }


    @Test
    @DisplayName("Should log exception when api fails")
    void testExceptionLogging(CapturedOutput output) {
        assertThatThrownBy(() -> employeeController.getEmployee(999))
                .isInstanceOf(Exception.class);

        String logOutput = output.toString();
        assertThat(logOutput).contains("✗ [CONTROLLER]");
        assertThat(logOutput).contains("failed");
        assertThat(logOutput).contains("EmployeeController.getEmployee()");
    }

}