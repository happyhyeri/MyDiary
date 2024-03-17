package org.edupoll.app.data;


import java.util.List;

import org.edupoll.app.entity.Image;
import org.edupoll.app.entity.Memo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MemoImage {
	private Memo memo;
	private List<Image> images;
}
