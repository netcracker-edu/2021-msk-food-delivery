import { Divider, Row, Col, Card } from "antd";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import WarehouseClient from "../../api/WarehouseClient";

import "./styles/style.css";

const WarehouseList = ({auth}) => {
    const warehouseClient = new WarehouseClient(auth);

    const [warehouses, setWarehouses] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    const navigate = useNavigate();
    
    async function fetchData(){
        setIsLoading(true);
        let response = await warehouseClient.getAllActive();
        setWarehouses(response);
        setIsLoading(false);
    }

    useEffect(() => {
        fetchData()
    }, []);

    return (

        <div className="warehouse_list_wrapper">
            {isLoading ? <></> : 
                <><h1>Active warehouses</h1>
                {
                    !warehouses.length ? <p>No active warehouses at the moment.</p> 
                    : 
                    <div className="warehouse_list_cards">
                    <Row gutter={[12, 24]}>  
                    {
                        warehouses.map((warehouse) => 
                               
                                <Col xs={{ span: 12}} sm={{ span: 8}} lg={{ span: 6}} >
                                    <Card hoverable className="warehouse_list_card" 
                                    onClick={() => navigate(`/warehouses/${warehouse.id}`)}>
                                        <h2>{`${warehouse.name}`}</h2>
                                        <Divider />
                                        <h3>{`${warehouse.address}`}</h3>
                                    </Card>
                                </Col>
                        )
                    }
                    </Row>    
                    </div>
                }</>
            }
        </div>
    );
}

export default WarehouseList;