package com.ssafy.enjoytrip.travelrequest.service.planstructure;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyPlanSkeleton {
	private LocalDate date;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	@Builder.Default
	private List<TimeBlock> blocks = new ArrayList<>();
	
	private boolean hasFixedLunch;
	private boolean hasFixedDinner;
	private boolean hasLodgingAtStart;
	private boolean hasLodgingAtEnd;
	
	public void fillOpenBlocks() {
		if (blocks == null) return;
		
		// 기존 고정 블록 정렬
	    blocks.sort(Comparator.comparing(TimeBlock::getStart));

	    List<TimeBlock> full = new ArrayList<>();
	    LocalDateTime cursor = startTime;

	    for (TimeBlock block : blocks) {
	        if (cursor.isBefore(block.getStart())) {
	            // 사이에 빈 시간이 있으면 open block 생성
	            full.add(TimeBlock.builder()
	                .start(cursor)
	                .end(block.getStart())
	                .isFixed(false)
	                .build());
	        }

	        full.add(block); // 고정 block 추가
	        cursor = block.getEnd();
	    }

	    // 마지막에 남은 open block
	    if (cursor.isBefore(endTime)) {
	        full.add(TimeBlock.builder()
	            .start(cursor)
	            .end(endTime)
	            .isFixed(false)
	            .build());
	    }

	    blocks = full;
		
	}
}
