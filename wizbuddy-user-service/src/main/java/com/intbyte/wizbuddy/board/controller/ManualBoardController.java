package com.intbyte.wizbuddy.board.controller;

import com.intbyte.wizbuddy.board.domain.DeleteManualBoardInfo;
import com.intbyte.wizbuddy.board.domain.EditManualBoardInfo;
import com.intbyte.wizbuddy.board.dto.ManualBoardDTO;
import com.intbyte.wizbuddy.board.service.ManualBoardService;
import com.intbyte.wizbuddy.board.vo.request.RequestInsertManualBoardVO;
import com.intbyte.wizbuddy.board.vo.request.RequestUpdateManualBoardVO;
import com.intbyte.wizbuddy.board.vo.response.ResponseFindManualBoardVO;
import com.intbyte.wizbuddy.board.vo.response.ResponseInsertManualBoardVO;
import com.intbyte.wizbuddy.board.vo.response.ResponseUpdateManualBoardVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manualboard")
public class  ManualBoardController {

    private final ManualBoardService manualBoardService;

    public ManualBoardController(ManualBoardService manualBoardService) {
        this.manualBoardService = manualBoardService;
    }

    @Operation(summary = "매뉴얼 게시판 전체 조회")
    @GetMapping
    public ResponseEntity<List<ResponseFindManualBoardVO>> getAllManualBoards() {
        List<ManualBoardDTO> manualBoardDTOs = manualBoardService.findAllManualBoards();

        List<ResponseFindManualBoardVO> manualBoardVOs = manualBoardDTOs.stream()
                .map(dto -> ResponseFindManualBoardVO.builder()
                        .manualCode(dto.getManualCode())
                        .manualTitle(dto.getManualTitle())
                        .manualContents(dto.getManualContents())
                        .manualFlag(dto.isManualFlag())
                        .imageUrl(dto.getImageUrl())
                        .createdAt(dto.getCreatedAt())
                        .updatedAt(dto.getUpdatedAt())
                        .shopCode(dto.getShopCode())
                        .userCode(dto.getUserCode())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(manualBoardVOs);
    }

    @Operation(summary = "매장별 매뉴얼 게시판 조회")
    @GetMapping("/shop/{shopcode}")
    public ResponseEntity<List<ResponseFindManualBoardVO>> getManualBoardByShopCode(@PathVariable("shopcode") int shopCode) {
        List<ManualBoardDTO> manualBoardDTOs = manualBoardService.findManualBoardByShopCode(shopCode);

        List<ResponseFindManualBoardVO> manualBoardVOs = manualBoardDTOs.stream()
                .map(dto -> ResponseFindManualBoardVO.builder()
                        .manualCode(dto.getManualCode())
                        .manualTitle(dto.getManualTitle())
                        .manualContents(dto.getManualContents())
                        .manualFlag(dto.isManualFlag())
                        .imageUrl(dto.getImageUrl())
                        .createdAt(dto.getCreatedAt())
                        .updatedAt(dto.getUpdatedAt())
                        .shopCode(dto.getShopCode())
                        .userCode(dto.getUserCode())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(manualBoardVOs);
    }

    @GetMapping("/{manualcode}")
    @Operation(summary = "매뉴얼 게시글 단 건 조회")
    public ResponseEntity<ResponseFindManualBoardVO> findManualBoardByManualCode(@PathVariable("manualcode") int manualCode) {
        ManualBoardDTO manualBoardDTO = manualBoardService.findManualBoardByManualCode(manualCode);

        ResponseFindManualBoardVO manualBoardVO =  ResponseFindManualBoardVO.builder()
                .manualCode(manualBoardDTO.getManualCode())
                .manualTitle(manualBoardDTO.getManualTitle())
                .manualContents(manualBoardDTO.getManualContents())
                .manualFlag(manualBoardDTO.isManualFlag())
                .imageUrl(manualBoardDTO.getImageUrl())
                .createdAt(manualBoardDTO.getCreatedAt())
                .updatedAt(manualBoardDTO.getUpdatedAt())
                .shopCode(manualBoardDTO.getShopCode())
                .userCode(manualBoardDTO.getUserCode())
                .build();

        return ResponseEntity.ok(manualBoardVO);
    }

    @PostMapping("/register")
    @Operation(summary = "매뉴얼 게시글 등록")
    public ResponseEntity<ResponseInsertManualBoardVO> insertManualBoard(
            @RequestBody RequestInsertManualBoardVO requestInsertManualBoardVO) {

        ResponseInsertManualBoardVO response = manualBoardService.registerManualBoard(requestInsertManualBoardVO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/update/{manualcode}")
    @Operation(summary = "매뉴얼 게시글 수정 및 삭제")
    public ResponseEntity<ResponseUpdateManualBoardVO> updateManualBoard(
            @PathVariable("manualcode") int manualCode,
            @RequestBody RequestUpdateManualBoardVO requestUpdateManualBoardVO) {

        if(requestUpdateManualBoardVO.isManualFlag()){

            EditManualBoardInfo editManualBoardInfo = new EditManualBoardInfo(
                    manualCode,
                    requestUpdateManualBoardVO.getManualTitle(),
                    requestUpdateManualBoardVO.getManualContents(),
                    requestUpdateManualBoardVO.getImageUrl(),
                    requestUpdateManualBoardVO.isManualFlag(),
                    LocalDateTime.now(),
                    requestUpdateManualBoardVO.getUserCode()
            );

            ResponseUpdateManualBoardVO response = manualBoardService.modifyManualBoard(manualCode, editManualBoardInfo);

            return ResponseEntity.ok(response);

        } else {
            DeleteManualBoardInfo deleteManualBoardInfo = new DeleteManualBoardInfo(
                    requestUpdateManualBoardVO.isManualFlag(),
                    LocalDateTime.now(),
                    requestUpdateManualBoardVO.getUserCode()
            );

            manualBoardService.deleteManualBoard(manualCode, deleteManualBoardInfo);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
