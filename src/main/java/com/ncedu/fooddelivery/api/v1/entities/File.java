package com.ncedu.fooddelivery.api.v1.entities;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@Entity
@Table(name = "files")
@TypeDef(
        name = "type_enum",
        typeClass = PostgreSQLEnumType.class
)
public class File {

    @Id
    @Column(name = "file_id")
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Type(type = "type_enum")
    private FileType type;
    private String name;
    private Long size;
    @Column(name = "upload_date")
    private Timestamp uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    private User owner;

    public File() {};
}
