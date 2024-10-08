package com.intbyte.wizbuddy.employeepershop.controller;

import com.intbyte.wizbuddy.employeepershop.domain.EditEmployeePerShopInfo;
import com.intbyte.wizbuddy.employeepershop.dto.EmployeePerShopDTO;
import com.intbyte.wizbuddy.employeepershop.service.EmployeePerShopService;
import com.intbyte.wizbuddy.employeepershop.vo.request.RequestInsertEmployeePerShopVO;
import com.intbyte.wizbuddy.employeepershop.vo.request.RequestModifyEmployeePerShopVO;
import com.intbyte.wizbuddy.employeepershop.vo.response.ResponseInsertEmployeePerShopVO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employeepershop")
public class EmployeePerShopController {

    private final EmployeePerShopService employeePerShopService;
    private final ModelMapper modelMapper;

    public EmployeePerShopController(EmployeePerShopService employeePerShopService, ModelMapper modelMapper) {
        this.employeePerShopService = employeePerShopService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseInsertEmployeePerShopVO> registerEmployeePerShop(@RequestBody RequestInsertEmployeePerShopVO request) {
        EmployeePerShopDTO employeePerShopDTO = modelMapper.map(request, EmployeePerShopDTO.class);

        employeePerShopService.insertEmployeePerShop(employeePerShopDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("list")
    public ResponseEntity<List<EmployeePerShopDTO>> getEmployeesPerShop() {
        List<EmployeePerShopDTO> employeePerShopDTOList = employeePerShopService.findAllEmployeePerShop();

        return ResponseEntity.status(HttpStatus.OK).body(employeePerShopDTOList);
    }

    @GetMapping("/{employeeCode}")
    public ResponseEntity<List<EmployeePerShopDTO>> getShopsbyEmployeeCode(@PathVariable String employeeCode) {
        List<EmployeePerShopDTO> employeePerShopDTO = employeePerShopService.findEmployeePerShopById(employeeCode);

        return ResponseEntity.status(HttpStatus.OK).body(employeePerShopDTO);
    }

    @GetMapping("shop/{shopCode}/employee/{employeeCode}")
    public ResponseEntity<EmployeePerShopDTO> getShopByEmployeeCode(@PathVariable int shopCode, @PathVariable String employeeCode) {
        EmployeePerShopDTO response = employeePerShopService.getEmployeePerShopByEmployeeCode(shopCode, employeeCode);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/modify/shop/{shopCode}/employee/{employeeCode}")
    public ResponseEntity<Void> modifyEmployeePerShop(@PathVariable int shopCode, @PathVariable String employeeCode, @RequestBody RequestModifyEmployeePerShopVO request) {
        EditEmployeePerShopInfo info = new EditEmployeePerShopInfo(request.getShopHourlyWage(), request.getShopMonthlyWage());

        employeePerShopService.editEmployeePerShopById(shopCode, employeeCode, info);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/employee/{employeeCode}")
    public ResponseEntity<Void> deleteEmployeePerShop(@PathVariable String employeeCode) {
        employeePerShopService.deleteEmployeePerShopByEmployeeCode(employeeCode);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
